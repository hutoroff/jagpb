package ru.hutoroff.jagpb.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.hutoroff.jagpb.data.model.PollDO;
import ru.hutoroff.jagpb.data.model.PollOption;
import ru.hutoroff.jagpb.data.model.Voter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PollInfoBuilder {

	public static SendMessage buildPollMessage(PollDO poll, Long chatId) {
		final String text = prepareText(poll);
		final SendMessage result = new SendMessage(chatId, text);

		if (poll.getOptions() != null) {
			final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
			List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(poll.getOptions().size());

			for (PollOption option : poll.getOptions()) {
				InlineKeyboardButton button = new InlineKeyboardButton(option.getTitle());
				button.setCallbackData("vote_" + option.getId());
				keyboard.add(Collections.singletonList(button));
			}
			keyboardMarkup.setKeyboard(keyboard);

			result.setReplyMarkup(keyboardMarkup);
		}

		return result;
	}

	private static String prepareText(PollDO poll) {
		StringBuilder sb = new StringBuilder(poll.getTitle()).append("\n\n");

		if (poll.getOptions() != null) {
			sb.append("Options:");
			for (PollOption pollOption : poll.getOptions()) {
				final List<Voter> voters = pollOption.getVoters() != null ? pollOption.getVoters() : Collections.emptyList();
				sb.append("\n* ").append(pollOption.getTitle()).append(" -- Voted: ").append(voters.size());
				for (Voter voter : voters) {
					sb.append("  -").append(voter.getFirstName()).append(" ").append(voter.getSecondName()).append(" (")
							.append(voter.getUsername()).append(")");
				}
			}
		}

		return sb.toString();
	}
}
